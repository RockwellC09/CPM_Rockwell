//
//  ProfileViewController.h
//  Watch List
//
//  Created by Christopher Rockwell on 6/5/14.
//  Copyright (c) 2014 Christopher Rockwell. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Parse/Parse.h>

@interface ProfileViewController : UIViewController
{
    IBOutlet UILabel *userLabel;
    IBOutlet UILabel *emailLabel;
    IBOutlet UILabel *favLabel;
    IBOutlet UILabel *hrsLabel;
}

-(IBAction)onClick:(id)sender;
@end
