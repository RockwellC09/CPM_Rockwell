//
//  ItemViewController.m
//  Watch List
//
//  Created by Christopher Rockwell on 6/12/14.
//  Copyright (c) 2014 Christopher Rockwell. All rights reserved.
//

#import "ItemViewController.h"
Reachability *myNetworkReachability;
NetworkStatus myNetworkStatus;

@interface ItemViewController ()

@end

@implementation ItemViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    // white status bar
    [self setNeedsStatusBarAppearanceUpdate];
    
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

-(UIStatusBarStyle)preferredStatusBarStyle{
    return UIStatusBarStyleLightContent;
}

- (void) viewWillAppear:(BOOL)animated {
    // retrieve movie/show information and display it to the user
    NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
    [titleLabel setText:[prefs valueForKey:@"title"]];
    PFQuery *query = [PFQuery queryWithClassName:@"itemInfo"];
    [query whereKey:@"user" equalTo:[PFUser currentUser]];
    [query whereKey:@"title" equalTo:titleLabel.text];
    [query findObjectsInBackgroundWithBlock:^(NSArray *objects, NSError *error) {
        if (!error) {
            for (PFObject *object in objects) {
                NSString *showingStr = [NSString stringWithFormat:@"Showing %@ at %@", [object objectForKey:@"day"], [object objectForKey:@"time"]];
                [showing setText:showingStr];
            }
        } else {
            NSString *errorString = [error userInfo][@"error"];
            // show the errorString
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error"
                                                            message:errorString
                                                           delegate:nil
                                                  cancelButtonTitle:@"OK"
                                                  otherButtonTitles:nil];
            [alert show];
        }
        
    }];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(IBAction)onClick:(id)sender {
    UIButton *button = (UIButton*) sender;
    
    if (button.tag == 0) {
        [self dismissViewControllerAnimated:true completion:nil];
    } else {
        // check network connection
        myNetworkReachability = [Reachability reachabilityWithHostName:@"www.google.com"];
        myNetworkStatus = [myNetworkReachability currentReachabilityStatus];
        if (myNetworkStatus == NotReachable) {
            // no connection
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"No Network Connection"
                                                            message:@"Please check your connection and try again."
                                                           delegate:nil
                                                  cancelButtonTitle:@"OK"
                                                  otherButtonTitles:nil];
            [alert show];
        } else {
            // has a valid connection
            
            // delete movie
            PFQuery *query = [PFQuery queryWithClassName:@"itemInfo"];
            [query whereKey:@"user" equalTo:[PFUser currentUser]];
            [query whereKey:@"title" equalTo:titleLabel.text];
            [query findObjectsInBackgroundWithBlock:^(NSArray *objects, NSError *error) {
                if (!error) {
                    for (PFObject *object in objects) {
                        [object delete];
                    }
                    UIStoryboard *storyBoard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
                    
                    ItemViewController *watchListView = [storyBoard instantiateViewControllerWithIdentifier:@"WatchListView"];
                    [self presentViewController:watchListView animated:true completion:nil];
                } else {
                    NSString *errorString = [error userInfo][@"error"];
                    // show the errorString
                    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error"
                                                                    message:errorString
                                                                   delegate:nil
                                                          cancelButtonTitle:@"OK"
                                                          otherButtonTitles:nil];
                    [alert show];
                }
                
            }];
        }
    }
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
