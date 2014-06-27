//
//  WatchListViewController.m
//  Watch List
//
//  Created by Christopher Rockwell on 6/12/14.
//  Copyright (c) 2014 Christopher Rockwell. All rights reserved.
//

#import "WatchListViewController.h"
int count;
int count2;
NSMutableArray *itemsArray;
Reachability *myNetworkReachability;
NetworkStatus myNetworkStatus;

@interface WatchListViewController ()

@end

@implementation WatchListViewController

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
    NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
        NSLog(@"get local data");
        itemsArray = [[NSMutableArray alloc] initWithArray:[prefs valueForKey:@"title"] copyItems:true];
        count = itemsArray.count;
        [myTableView reloadData];
    
    // white status bar
    [self setNeedsStatusBarAppearanceUpdate];
    
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)viewWillAppear:(BOOL)animated {
    // defaults
    count = 0;
    count2 = 0;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(UIStatusBarStyle)preferredStatusBarStyle{
    return UIStatusBarStyleLightContent;
}

// number of table view rows
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return count;
}

// populate table view
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *simpleTableIdentifier = @"SimpleTableCell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:simpleTableIdentifier];
    }
    cell.textLabel.textColor = [UIColor darkGrayColor];
    
    // set table view cell text to movie titles in items array
    cell.textLabel.text = [NSString stringWithFormat:@"%@", [itemsArray objectAtIndex:count2]];
    count2++;
    
    return cell;
}

// handle table view selection
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
    UITableViewCell *cell = [tableView cellForRowAtIndexPath:indexPath];
    NSString *movTitle = cell.textLabel.text;
    [prefs setObject:movTitle forKey:@"movTitle"];
    
    UIStoryboard *storyBoard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
    
    WatchListViewController *itemView = [storyBoard instantiateViewControllerWithIdentifier:@"ItemView"];
    [self presentViewController:itemView animated:true completion:nil];
}

-(IBAction)onClick:(id)sender {
    UIButton *button = (UIButton *)sender;
    if (button.tag == 0) {
        UIStoryboard *storyBoard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
        
        WatchListViewController *addMovieView = [storyBoard instantiateViewControllerWithIdentifier:@"AddMovieView"];
        [self presentViewController:addMovieView animated:true completion:nil];
    } else {
        UIStoryboard *storyBoard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
        
        WatchListViewController *profileView = [storyBoard instantiateViewControllerWithIdentifier:@"ProfileView"];
        [self presentViewController:profileView animated:true completion:nil];
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
